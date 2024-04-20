import {Button, FormControl, Modal} from "react-bootstrap";
import Image from "react-bootstrap/Image";
import addFriendIcon from "../../Assets/add-friend-icon.png";
import {useState} from "react";
import axiosInstance from "../../AxiosConfig";

export default function FriendPanel(props){
    const [show, setShow] = useState(false);
    const handleClose = () => {
        setShow(false);
        setQuery('')
        setSearchResult([])
    }
    const handleShow = () => setShow(true);
    const [query, setQuery] = useState('')
    function handleQueryChange(e){
        setQuery(e.target.value)
    }

    const [searchResult, setSearchResult] = useState([])

    async function search() {
        const token = localStorage.getItem('token')
        try {
            const response = await axiosInstance.get(`/user/search/${query}`, {
                headers: {
                  Authorization: `Bearer ${token}`
                },
                withCredentials: true
            })
            console.log(response.data)
            setSearchResult(response.data)
        } catch (e) {
            console.log(e)
        }
    }

    function sendFriendRequest(to){
        props.client.publish({
            destination: '/app/private',
            body: JSON.stringify({
                from: localStorage.getItem('loggedUser'),
                to: to,
                content: '',
                type: 'FRIEND_REQUEST',
            })
        })
    }

    async function updateChatPanelData(name, imageUrl, email, id, type){
        // const message = await axiosInstance.get(`/user/conversation/content/${id}`, {
        //     headers: {
        //         Authorization: `Bearer ${localStorage.getItem('token')}`
        //     }
        // })
        props.setChatPanelData({
            messages: [],
            conversationId: id,
            friendName: name,
            friendImageUrl: imageUrl,
            friendEmail: email,
            type: type
        })
    }

    return (
        <div id={'friend-list-wrapper'}>
            <div id={'friend-search'}>
                <FormControl type={'text'} placeholder={'Search friends'}/>
                <Image id={'add-friend-btn'} onClick={handleShow} src={addFriendIcon}/>
            </div>
            <Modal id={'find-friend-panel'} show={show} onHide={handleClose} backdrop="static" keyboard={false} centered>
                <Modal.Header>
                    <Modal.Title>Add friend</Modal.Title>
                </Modal.Header>
                <Modal.Body style={{minHeight: '60dvh'}}>
                    <FormControl onChange={handleQueryChange} value={query} type={'text'} placeholder={'Enter friend\'s name'}/>
                    <div id={'search-result'}>
                        {searchResult.map((user, index) => {
                            return (
                                <div key={index} className={'search-result-item'}>
                                    <div style={{display: 'flex', columnGap: '1rem'}}>
                                        <div style={{position: 'relative'}}>
                                            <Image className={'user-profile-img'} src={user.imageUrl} roundedCircle/>
                                            <div
                                                style={{backgroundColor: user.userStatus === 'INACTIVE' ? 'gray' : 'lightgreen'}}
                                                className={'user-status'}></div>
                                        </div>
                                        <p className={'friend-name'}>{user.lastName + ' ' + user.firstName}</p>
                                    </div>
                                    <Button onClick={() => sendFriendRequest(user.email)} className={'action-btn'}
                                            variant="success">
                                        Add Friend
                                    </Button>
                                </div>
                            )
                        })}
                    </div>
                </Modal.Body>
                <Modal.Footer>
                <Button onClick={search} variant="dark">Find</Button>
                    <Button variant="danger" onClick={handleClose}>Cancel</Button>
                </Modal.Footer>
            </Modal>
            <div id={'friend-list'}>
                {props.conversationData.length !== 0 ?
                    props.conversationData.map((item, index) => {
                        return (
                            <div key={index} className={'friend-item'}
                                 onClick={() => updateChatPanelData(item.friendName, item.friendImageUrl, item.friendEmail, item.id, item.conversationType)}>
                                <div style={{display: 'flex', columnGap: '1rem', alignItems: 'center'}}>
                                    <Image className={'user-profile-img'} src={item.friendImageUrl} roundedCircle/>
                                    <p className={'friend-name'}>{item.friendName}</p>
                                </div>
                            </div>
                        )
                    }) : <p style={{paddingInline: '1rem'}}>You didn't have any friend yet. Add someone and start chatting!</p>
                }
            </div>
        </div>
    )
}