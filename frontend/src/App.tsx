import { Routes, Route, Navigate } from "react-router-dom";
import "./App.css";
import LoginPage from "./pages/LoginPage/LoginPage";
import Layout from "./components/Layout/Layout";
import Footer from "./components/Footer/Footer";
import { useAppSelector } from "./hooks/hooks";
import { selectJwt } from "./redux/authSlice";
import LogoutPage from "./pages/LogoutPage/LogoutPage";
import NewShortUrlPage from "./pages/NewShortUrlPage/NewShortUrlPage";
import Header from "./components/Header/Header";

function App() {
  const jwt = useAppSelector(selectJwt);
  const LayoutBody = () => (
    <Routes>
      <Route
        path="/login"
        element={jwt ? <Navigate to="/" /> : <LoginPage />}
      />
      <Route path="/logout" element={<LogoutPage />} />
      <Route path="/" element={<NewShortUrlPage />} />
      <Route
        path="/my-urls/"
        element={jwt ? <>MY URLS</> : <Navigate to="/login" />}
      />
      <Route
        path="/my-urls/:shortId"
        element={jwt ? <>VIEW SHORTEN URL</> : <Navigate to="/login" />}
      />
      <Route path="*" element={<Navigate to="/" />} />
    </Routes>
  );

  return (
    <Layout header={<Header />} body={<LayoutBody />} footer={<Footer />} />
  );
}

export default App;
