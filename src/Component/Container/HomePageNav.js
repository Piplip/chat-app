import {Link} from "react-router-dom";
import {Button} from "react-bootstrap";
import "../../Style/HomePage-styles.css"

export default function HomePageNav(){
    return (
        <div id={"content"}>
            <p>Expand your horizons. Explore new conversations.</p>
            <div id={'nav-btn'}>
                <Link to={'/login'}>
                    <Button id={'global-nav-btn'} variant="warning">Login</Button>
                </Link>
                <Link to={'/signup'}>
                    <Button id={'global-nav-btn'} variant="light">Sign Up</Button>
                </Link>
            </div>
        </div>
    )
}