import { NavLink } from "react-router-dom";
import styles from "./Header.module.css";
import { useLoggedIn } from "../../hooks/useLoggedIn";
import { ROUTES } from "../../routePaths";
import { useUser } from "../../hooks/useUser";

export default function Header() {
  const isLoggedIn = useLoggedIn();
  const user = useUser();

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
          to={ROUTES.home}
          end
        >
          Shorten an URL
        </NavLink>
        {isLoggedIn && (
          <NavLink
            className={({ isActive, isPending }) => {
              const match =
                isActive || location.pathname.startsWith("/my-urls");

              return isPending
                ? `${styles.link} ${styles.pending}`
                : match
                ? `${styles.link} ${styles.active}`
                : styles.link;
            }}
            to={ROUTES.userShortUrlList}
            end
          >
            My short URLs
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
            to={ROUTES.login}
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
            to={ROUTES.logout}
            end
          >
            Logout ({user?.email})
          </NavLink>
        )}
      </nav>
    </div>
  );
}
