import "../Style/HomePage-styles.css"
import {Outlet} from "react-router";

export default function HomePage(){
    return (
        <div id={"homepage"}>
            <Outlet />
        </div>
    )
}