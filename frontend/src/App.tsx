import { Routes, Route, Navigate } from "react-router-dom";
import "./App.css";
import Page from "./components/Page/Page";
import LoginPage from "./pages/LoginPage/LoginPage";
import Layout from "./components/Layout/Layout";
import Navbar from "./components/Navbar/Navbar";
import Footer from "./components/Footer/Footer";
import { useAppSelector } from "./hooks/hooks";
import { selectJwt } from "./redux/authSlice";
import LogoutPage from "./pages/LogoutPage/LogoutPage";
import NewShortUrlPage from "./pages/NewShortUrlPage/NewShortUrlPage";

function App() {
  const jwt = useAppSelector(selectJwt);

  const LoginPageImpl = () => (
    <Page>
      <LoginPage />
    </Page>
  );
  const LogoutPageImpl = () => (
    <Page>
      <LogoutPage />
    </Page>
  );
  const NewShortUrlPageImpl = () => (
    <Page>
      <NewShortUrlPage />
    </Page>
  );

  const LayoutBody = () => (
    <Routes>
      <Route
        path="/login"
        element={jwt ? <Navigate to="/" /> : <LoginPageImpl />}
      />
      <Route path="/logout" element={<LogoutPageImpl />} />
      <Route path="/" element={<NewShortUrlPageImpl />} />
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
    <Layout header={<Navbar />} body={<LayoutBody />} footer={<Footer />} />
  );
}

export default App;
