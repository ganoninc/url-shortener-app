import { useRoutes } from "react-router-dom";
import "./App.css";
import Layout from "./components/Layout/Layout";
import Footer from "./components/Footer/Footer";
import { useAppSelector } from "./hooks/hooks";
import { selectJwt } from "./redux/authSlice";
import Header from "./components/Header/Header";
import { routes } from "./routes";

function App() {
  const jwt = useAppSelector(selectJwt);
  const element = useRoutes(routes(jwt));

  return <Layout header={<Header />} body={element} footer={<Footer />} />;
}

export default App;
