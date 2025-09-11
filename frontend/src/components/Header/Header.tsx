import { NavLink } from "react-router-dom";
import styles from "./Header.module.css";
import { useAppSelector } from "../../hooks/hooks";
import { selectJwt } from "../../redux/authSlice";

export default function Header() {
  const isLoggedIn = useAppSelector(selectJwt);

  return (
    <div className={styles.navbar}>
      <span className={styles.appName}>Vite URL Shortener</span>
      <nav>
        <NavLink
          className={({ isActive, isPending }) =>
            isPending
              ? `${styles.link} ${styles.pending}`
              : isActive
              ? `${styles.link} ${styles.active}`
              : styles.link
          }
          to="/"
          end
        >
          Shorten an URL
        </NavLink>
        {isLoggedIn && (
          <NavLink
            className={({ isActive, isPending }) =>
              isPending
                ? `${styles.link} ${styles.pending}`
                : isActive
                ? `${styles.link} ${styles.active}`
                : styles.link
            }
            to="/my-urls"
            end
          >
            My URLs
          </NavLink>
        )}
        {!isLoggedIn && (
          <NavLink
            className={({ isActive, isPending }) =>
              isPending
                ? `${styles.link} ${styles.pending}`
                : isActive
                ? `${styles.link} ${styles.active}`
                : styles.link
            }
            to="/login"
            end
          >
            Login
          </NavLink>
        )}
        {isLoggedIn && (
          <NavLink
            className={({ isActive, isPending }) =>
              isPending
                ? `${styles.link} ${styles.pending}`
                : isActive
                ? `${styles.link} ${styles.active}`
                : styles.link
            }
            to="/logout"
            end
          >
            Logout
          </NavLink>
        )}
      </nav>
    </div>
  );
}
