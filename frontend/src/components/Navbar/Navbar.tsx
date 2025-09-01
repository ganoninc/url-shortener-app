import { NavLink } from "react-router-dom";
import "./Navbar.css";
import { useAppSelector } from "../../hooks/hooks";
import { selectJwt } from "../../redux/authSlice";

export default function Navbar() {
  const isLoggedIn = useAppSelector(selectJwt);

  return (
    <div className="navbar">
      <span className="app-name">Vite URL Shortener</span>
      <nav>
        <NavLink className="link" to="/" end>
          Shorten an URL
        </NavLink>
        {isLoggedIn && (
          <NavLink className="link" to="/my-urls" end>
            My URLs
          </NavLink>
        )}
        {!isLoggedIn && (
          <NavLink className="link" to="/login" end>
            Login
          </NavLink>
        )}
        {isLoggedIn && (
          <NavLink className="link" to="/logout" end>
            Logout
          </NavLink>
        )}
      </nav>
    </div>
  );
}
