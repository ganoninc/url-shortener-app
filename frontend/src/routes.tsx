// src/routes.tsx
import { Navigate } from "react-router-dom";
import LoginPage from "./pages/LoginPage/LoginPage";
import LogoutPage from "./pages/LogoutPage/LogoutPage";
import NewShortUrlPage from "./pages/NewShortUrlPage/NewShortUrlPage";
import { ROUTES } from "./routePaths";

export const routes = (jwt: string | null) => [
  {
    path: ROUTES.login,
    element: jwt ? <Navigate to={ROUTES.home} /> : <LoginPage />,
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
    element: jwt ? <>MY URLS</> : <Navigate to={ROUTES.login} />,
  },
  {
    path: ROUTES.myUrlDetail(),
    element: jwt ? <>VIEW SHORTEN URL</> : <Navigate to={ROUTES.login} />,
  },
  {
    path: "*",
    element: <Navigate to={ROUTES.home} />,
  },
];
