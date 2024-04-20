import {RouterProvider} from "react-router";
import {createBrowserRouter, Link} from "react-router-dom"
import HomePage from "./Page/HomePage";
import ChatPage, {getUserData} from "./Page/ChatPage";
import HomePageNav from "./Component/Container/HomePageNav";
import 'bootstrap/dist/css/bootstrap.min.css';
import LoginForm from "./Component/Container/LoginForm";
import SignUpForm from "./Component/Container/SignUpForm";

function App() {
    const browserRouter = createBrowserRouter([
        {
            path: '/',
            element: <HomePage />,
            children: [
                {
                    index: true,
                    element: <HomePageNav />
                },
                {
                    path: '/login',
                    element: <LoginForm />
                },
                {
                    path: '/signup',
                    element: <SignUpForm />
                }
            ]
        },
        {
            path: '/verify/success',
            element: <div style={{width: '100%', height: '100dvh', backgroundColor: 'black', padding: '2rem', textAlign: 'center'}}>
                        <p style={{fontSize: '4rem', color: 'orange', marginBlock: '4rem'}}>EIDA</p>
                        <h1 style={{color: 'lightgreen'}}>VERIFIED SUCCESSFULLY</h1>
                        <p style={{color: 'white'}}>You can now close this page or head to <Link style={{color: 'yellow'}} to={'/login'}>login page.</Link></p>
                    </div>
        },
        {
            path: "/chat",
            element: <ChatPage />,
            loader: getUserData
        },
    ])
    return (
        <RouterProvider router={browserRouter}></RouterProvider>
    )
}

export default App;
