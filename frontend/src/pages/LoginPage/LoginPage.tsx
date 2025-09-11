import { setCredentials } from "../../redux/authSlice";
import Title from "../../components/Title/Title";
import { useAppDispatch } from "../../hooks/hooks";
import Button from "../../components/Button/Button";
import Page from "../../components/Page/Page";

export default function LoginPage() {
  const dispatch = useAppDispatch();
  const API_GATEWAY_URL = import.meta.env.VITE_API_GATEWAY_URL;

  function handleLogin() {
    const popup = window.open(
      API_GATEWAY_URL + "/auth/oauth2/authorization/google",
      "viteUrlShortenerOauthLogin",
      "width=500,height=600"
    );

    function receiveMessage(event: MessageEvent) {
      if (event.origin !== API_GATEWAY_URL) return;

      const { token, email } = event.data;
      if (token && email) {
        dispatch(
          setCredentials({
            jwt: token,
            user: { email },
          })
        );
      }

      window.removeEventListener("message", receiveMessage);
      popup?.close();
    }

    window.addEventListener("message", receiveMessage);
  }

  return (
    <Page>
      <Title content="Login" />
      <Button label="Login with Google" onClick={handleLogin} />
    </Page>
  );
}
