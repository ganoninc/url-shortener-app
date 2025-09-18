import { useRoutes } from "react-router-dom";
import "./App.css";
import Layout from "./components/Layout/Layout";
import Footer from "./components/Footer/Footer";
import Header from "./components/Header/Header";
import { routes } from "./routes";
import { useLoggedIn } from "./hooks/useLoggedIn";

function App() {
  const isLoggedIn = useLoggedIn();
  const element = useRoutes(routes(isLoggedIn));

  return <Layout header={<Header />} body={element} footer={<Footer />} />;
}

export default App;
