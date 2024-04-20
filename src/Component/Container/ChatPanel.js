import {FormControl, InputGroup} from "react-bootstrap";
import Image from "react-bootstrap/Image";
import friendIcon from "../../Assets/send-icon.png"
import {useState} from "react";

export default function ChatPanel(props){
    console.log("chat panel data", props.chatPanelData)
    const messages = props.chatPanelData.messages
    const [currentMessage, setCurrentMessage] = useState('')

    function sendMessage(){
        props.updateMessages(currentMessage, localStorage.getItem('loggedUser'))
        setCurrentMessage('')
        props.client.publish({
            destination: '/app/chat',
            body: JSON.stringify({
                conversationID: props.chatPanelData.conversationId,
                from: localStorage.getItem('loggedUser'),
                to: props.chatPanelData.friendEmail,
                content: currentMessage,
                type: props.chatPanelData.type
            })
        })
    }

    return (
        <div id={'chat-wrapper'}>
            <div id={'top-panel'}>
                <p>{props.chatPanelData.friendName}</p>
                <Image className={'user-profile-img'} src={props.chatPanelData.friendImageUrl} roundedCircle/>
            </div>
            <div id={'chat-zone'}>
                {messages.map((message, index) => {
                    return (
                        <div key={index} className={message.from === localStorage.getItem('loggedUser') ? 'my-message' : 'friend-message'}>
                            <p>{message.content}</p>
                        </div>
                    )
                })}
            </div>
            <div id={'bottom-panel'}>
                <InputGroup id={'message-inp-wrapper'}>
                    <FormControl value={currentMessage} id={"message-inp"} onChange={(e) => setCurrentMessage(e.target.value)}
                                 placeholder={`Chat with ${props.chatPanelData.friendName}`}/>
                    <Image onClick={sendMessage} id={'send-msg-btn'} src={friendIcon} />
                </InputGroup>
            </div>
        </div>
    )
}