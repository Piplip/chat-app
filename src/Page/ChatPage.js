import "../Style/ChatPage-styles.css"
import LeftNav from "../Component/Container/LeftNav";
import FriendPanel from "../Component/Container/FriendPanel";
import ChatPanel from "../Component/Container/ChatPanel";
import {Client} from "@stomp/stompjs";
import {useEffect, useMemo, useState} from "react";
import axiosInstance from "../AxiosConfig";
import {useLoaderData} from "react-router";

export function getUserData() {
    const email = localStorage.getItem('loggedUser');
    const token = localStorage.getItem('token');
    return Promise.all([
        axiosInstance.get('/user/fetch/notifications', {
            headers: {Authorization: `Bearer ${token}`},
            params: {email: email}
        }),
        axiosInstance.get('/user/fetch/conversations', {
            headers: {Authorization: `Bearer ${token}`},
            params: {email: email}
        })
    ]).then(([notificationsResponse, conversationResponse]) => {
        const notifications = notificationsResponse.data;
        const conversations = conversationResponse.data;
        console.log("loader conversation data:", conversationResponse.data)
        return { notifications, conversations };
    }).catch(error => {
        console.error('Error fetching data:', error);
        throw error;
    });
}

export default function ChatPage(){
    const loader = useLoaderData()

    const [notificationData, setNotificationData] = useState(loader.notifications)
    const [conversationData, setConversationData] = useState(loader.conversations)
    const [chatPanelData, setChatPanelData] = useState({
        messages: [
            {
                content: null,
                from: null,
            }
        ],
        conversationId: null,
        friendName: null,
        friendEmail: null,
        friendImageUrl: null,
        type: null,
    })

    const client = useMemo(() => new Client({
        brokerURL: 'ws://localhost:8080/ws',
        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
    }), []);

    useEffect(() => {
        client.onConnect = function() {
            const email = localStorage.getItem('loggedUser')
            client.subscribe(`/user/${email}/specific`, function (message) {
                try {
                    const parsedMessage = JSON.parse(message.body)
                    if (parsedMessage.conversationType === 'SINGLE' || parsedMessage.conversationType === 'GROUP') {
                        console.log("conversation response", parsedMessage)
                        setConversationData(conversationData => [parsedMessage, ...conversationData])
                    }
                    else if(parsedMessage.notificationType === 'FRIEND_REQUEST' || parsedMessage.notificationType === 'MESSAGE'
                        || parsedMessage.notificationType === 'ACCEPT_FRIEND_REQUEST' || parsedMessage.notificationType === 'DECLINE_FRIEND_REQUEST'
                    ){
                        console.log("notification response 1", parsedMessage)
                        setNotificationData(prevNotificationData => [parsedMessage, ...prevNotificationData])
                    }
                    else{
                        console.log("message response", parsedMessage)
                        updateMessage(parsedMessage.content, parsedMessage.from)
                    }

                }catch (e){
                    console.log(e)
                }
            })
        }
    }, [client]);
    if(!client.connected){
        client.activate()
    }

    function updateMessage(message, from){
        setChatPanelData(chatPanelData => {
            return {
                ...chatPanelData,
                messages: [...chatPanelData.messages, {content: message, from: from}]
            }
        })
    }

    return (
        <div id={'chat-page'}>
            <LeftNav client={client} notificationData={notificationData} setNotificationData={setNotificationData}/>
            <FriendPanel conversationData={conversationData} client={client} setChatPanelData={setChatPanelData}/>
            {chatPanelData.conversationId === null ?
                <div style={{display: "grid", placeItems: 'center', color: 'white', fontSize: '1.25rem'}}>
                    <p>Select a friend to start chatting</p>
                </div> : <ChatPanel client={client} chatPanelData={chatPanelData} updateMessages={updateMessage}/>}
        </div>
    )
}