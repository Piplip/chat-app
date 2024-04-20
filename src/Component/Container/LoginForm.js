import {Button, Form, FormControl, FormGroup, FormLabel, FormText, Spinner} from "react-bootstrap";
import {Link, useNavigate} from "react-router-dom";
import {useState} from "react";
import axiosInstance from "../../AxiosConfig";

export default function LoginForm(){
    const [showSpinner, setShowSpinner] = useState(false)
    const [showTooltip, setShowTooltip] = useState(false)
    const [loginData, setLoginData] = useState({
        email: '',
        password: ''
    })
    const navigate = useNavigate()
    function handleChange(e){
        setLoginData({
            ...loginData,
            [e.target.id]: e.target.value
        })
    }

    const [showInvalid, setShowInvalid] = useState(false)
    async function sendLoginRequest(){
        setShowSpinner(true)
        try{
            const response  = await axiosInstance.post("/user/login", {
                email: loginData.email,
                password: loginData.password
            })
            if(response.data.status === "OK"){
                localStorage.setItem('token', response.data.data.jwt)
                localStorage.setItem('loggedUser', loginData.email)
                navigate('/chat')
            }
        }catch (e) {
            setShowInvalid(true)
            setShowSpinner(false)
        }
    }

    return (
        <div id={'login-form'}>
            <div id={'login-form-wrapper'}>
                <h1>LOGIN</h1>
                <Form id={"form-wrapper"}>
                    {
                        showInvalid && <FormText style={{color: 'red', textAlign: 'center'}}>Account not verified</FormText>
                    }
                    <FormGroup>
                        <FormLabel htmlFor="email">Email</FormLabel>
                        <FormControl onChange={handleChange} name={'email'} value={loginData.email} type="email" id="email"/>
                    </FormGroup>
                    <FormGroup>
                        <FormLabel htmlFor="password">Password</FormLabel>
                        <FormControl onChange={handleChange} name={'password'} value={loginData.password} type="password" id="password"/>
                        {showTooltip && <FormText id={'form-tooltip'}>Incorrect email or password. Please try again.</FormText>}
                    </FormGroup>
                    <Button onClick={sendLoginRequest} style={{marginTop: '1.25rem'}} variant={"success"}>
                        {showSpinner ? <Spinner animation="border" size="sm" /> : 'Login'}
                    </Button>
                </Form>
                <div id={'form-bottom-nav'}>
                    <p id={'form-nav'}>Forgot password?</p>
                    <p id={'form-nav'}>Don't have an account? <Link className={'global-link'} to={'/signup'}>Sign up </Link>here</p>
                </div>
            </div>
        </div>
    )
}