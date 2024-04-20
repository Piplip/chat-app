import Image from "react-bootstrap/Image";
import messageIcon from "../../Assets/message-icon.png";
import settingIcon from "../../Assets/setting-icon.png";
import notificationIcon from "../../Assets/notification-icon.png"
import {Button, Col, Offcanvas, Row} from "react-bootstrap";
import {useState} from "react";

export default function LeftNav(props){
    const [show, setShow] = useState(false);
    const [showBtn, setShowBtn] = useState(true)
    const handleClose = () => {
        setShow(false);
    }

    function handleFriendRequest(type, to, index){
        setShowBtn(false)
        props.setNotificationData(prevNotificationData => {
            const newNotificationData = [...prevNotificationData]
            newNotificationData.splice(index, 1)
            return newNotificationData
        })
        props.client.publish({
            destination: '/app/private',
            body: JSON.stringify({
                from: localStorage.getItem('loggedUser'),
                to: to,
                content: '',
                type: type ? 'ACCEPT_FRIEND_REQUEST' : 'DECLINE_FRIEND_REQUEST'
            })
        })
    }

    const handleShow = () => setShow(true);
    return (
        <div id={'left-nav'}>
            <Image style={{width: '75%', marginBottom: '1rem'}}
                   src="https://static-00.iconduck.com/assets.00/user-icon-2048x2048-ihoxz4vq.png"
                   roundedCircle/>
            <Image className={'nav-btn'} src={messageIcon}/>
            <Image className={'nav-btn'} src={settingIcon}/>
            <div style={{position: 'relative', width: '100%', display: 'flex', justifyContent: 'center'}}>
                <Image onClick={handleShow} className={'nav-btn'} src={notificationIcon}/>
                <div className={'notification-count'}>{props.notificationData.length}</div>
            </div>
            <Offcanvas id={'notification-panel'} show={show} onHide={handleClose}>
                <Offcanvas.Header>
                    <Offcanvas.Title style={{fontSize: '2rem'}}>Notifications</Offcanvas.Title>
                </Offcanvas.Header>
                <Offcanvas.Body id={'notification-panel-body'}>
                    {props.notificationData.map((notification, index) => {
                        return (
                            <div key={index} className={'notification-item'}>
                                <div style={{display: 'flex', alignItems: 'center', columnGap: '1rem', maxWidth: "70%"}}>
                                    <Image className={'notify-user-profile-img'} src={notification.senderImageUrl} roundedCircle/>
                                    <p>{notification.content}</p>
                                </div>
                                {notification.notificationType === 'FRIEND_REQUEST' && showBtn ?
                                    <Col style={{display: 'flex', flexDirection: 'column', rowGap: '0.5rem', flexGrow: 0}}>
                                       <Row><Button onClick={() => handleFriendRequest(true, notification.senderEmail, index)} variant={'light'}>Accept</Button></Row>
                                        <Row><Button onClick={() => handleFriendRequest(false, notification.senderEmail, index)} variant={'dark'}>Decline</Button></Row>
                                    </Col> : null}
                            </div>
                        )}
                    )}
                </Offcanvas.Body>
            </Offcanvas>
        </div>
    )
}