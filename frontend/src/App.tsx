import { Routes, Route, Navigate } from "react-router-dom";
import "./App.css";
import Page from "./components/Page/Page";
import LoginPage from "./pages/LoginPage/LoginPage";
import Layout from "./components/Layout/Layout";
import Navbar from "./components/Navbar/Navbar";
import Footer from "./components/Footer/Footer";
import { useAppSelector } from "./hooks/hooks";
import { selectJwt } from "./redux/authSlice";

function App() {
  const jwt = useAppSelector(selectJwt);

  const LoginPageImpl = () => (
    <Page>
      <LoginPage></LoginPage>
    </Page>
  );

  const LayoutBody = () => (
    <Routes>
      <Route
        path="/login"
        element={jwt ? <Navigate to="/new" /> : <LoginPageImpl />}
      />
      <Route path="/new" element={<>NEW URL</>} />
      <Route path="/" element={<>NEW URL</>} />
      <Route
        path="/short/:shortId"
        element={jwt ? <>VIEW SHORTEN URL</> : <Navigate to="/login" />}
      />
      <Route
        path="/my-urls/"
        element={jwt ? <>MY URLS</> : <Navigate to="/login" />}
      />
      <Route path="*" element={<Navigate to="/" />} />
    </Routes>
  );

  return (
    <Layout header={<Navbar />} body={<LayoutBody />} footer={<Footer />} />
  );
}

export default App;
