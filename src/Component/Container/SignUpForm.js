import {Button, Col, Form, FormControl, FormGroup, FormLabel, FormText, Modal, Row, Spinner} from "react-bootstrap";
import {useState} from "react";
import axiosInstance from "../../AxiosConfig";
import {Link} from "react-router-dom";

export default function SignUpForm(){
    const [signupData, setSignupData] = useState({
        firstName: '',
        lastName: '',
        email: '',
        password: ''
    })
    const [show, setShow] = useState(false);
    const handleClose = () => setShow(false);
    const handleShow = () => setShow(true);

    function handleChange(e){
        setSignupData({
            ...signupData,
            [e.target.name]: e.target.value
        })
    }

    async function sendSignUpRequest(){
        setShowSpinner(true)
        const response = await axiosInstance.post('/user/register', {
            firstName: signupData.firstName,
            lastName: signupData.lastName,
            email: signupData.email,
            password: signupData.password
        })
        if(response.data.status === "CREATED"){
            setShowSpinner(false)
            handleShow()
            setSignupData({
                firstName: '',
                lastName: '',
                email: '',
                password: ''
            })
        }
    }

    const [showSpinner, setShowSpinner] = useState(false)
    return (
        <div id={'signup-form'}>
            <div id={'signup-form-wrapper'}>
                <h1>SIGN UP</h1>
                <Form id={"form-wrapper"}>
                    <FormGroup>
                        <Row>
                            <Col>
                                <FormLabel htmlFor="firstname">First name</FormLabel>
                                <FormControl onChange={handleChange} name={'firstName'} value={signupData.firstName} type="text" id="firstname" />
                            </Col>
                            <Col>
                                <FormLabel htmlFor="lastname">Last name</FormLabel>
                                <FormControl onChange={handleChange} name={'lastName'} value={signupData.lastName} type="text" id="lastname" />
                            </Col>
                        </Row>
                    </FormGroup>
                    <FormGroup>
                        <FormLabel htmlFor="email">Email</FormLabel>
                        <FormControl onChange={handleChange} name={'email'} value={signupData.email} type="email" id="email" aria-describedby="emailHelpBlock"/>
                    </FormGroup>
                    <FormGroup>
                        <FormLabel htmlFor="password">Password</FormLabel>
                        <FormControl onChange={handleChange} name={'password'} value={signupData.password} type="password" id="password" aria-describedby="passwordHelpBlock"/>
                    </FormGroup>
                    <Button style={{marginTop: '1rem'}} variant={"success"} onClick={sendSignUpRequest}>
                        {showSpinner ? <Spinner animation="border" size="sm" /> : 'Sign up'}
                    </Button>
                </Form>
                <div id={'form-bottom-nav'}>
                    <p id={'form-nav'}>Forgot password?</p>
                    <p id={'form-nav'}>Already have an account? <Link className={'global-link'} to={'/login'}>Log in </Link> here</p>
                </div>
            </div>
            <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                    <Modal.Title>Successfully Create New Account</Modal.Title>
                </Modal.Header>
                <Modal.Body>Please go to your email and follow the instructions to verify your account.</Modal.Body>
                <Modal.Footer>
                    <Button variant="dark" onClick={handleClose}>
                        Proceed
                    </Button>
                </Modal.Footer>
            </Modal>
        </div>
    )
}