// src/routes.tsx
import { Navigate } from "react-router-dom";
import LoginPage from "./pages/LoginPage/LoginPage";
import LogoutPage from "./pages/LogoutPage/LogoutPage";
import NewShortUrlPage from "./pages/NewShortUrlPage/NewShortUrlPage";
import { ROUTES } from "./routePaths";
import ShortUrlPage from "./pages/ShortUrlPage/ShortUrlPage";

export const routes = (isLoggedIn: boolean | null) => [
  {
    path: ROUTES.login,
    element: isLoggedIn ? <Navigate to={ROUTES.home} /> : <LoginPage />,
  },
  {
    path: ROUTES.logout,
    element: <LogoutPage />,
  },
  {
    path: ROUTES.home,
    element: <NewShortUrlPage />,
  },
  {
    path: ROUTES.myUrls,
    element: isLoggedIn ? <>MY URLS</> : <Navigate to={ROUTES.login} />,
  },
  {
    path: ROUTES.myUrlDetail(),
    element: isLoggedIn ? <ShortUrlPage /> : <Navigate to={ROUTES.login} />,
  },
  {
    path: "*",
    element: <Navigate to={ROUTES.home} />,
  },
];
