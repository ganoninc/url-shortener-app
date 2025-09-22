import { setCredentials } from "../../redux/authSlice";
import Title from "../../components/Title/Title";
import { useAppDispatch } from "../../hooks/hooks";
import Button from "../../components/Button/Button";
import Page from "../../components/Page/Page";
import { fakeAuthenticatedAuthState } from "../../redux/fakes";
import { isMSWEnabled } from "../../config/msw";
import { apiGatewayUrl } from "../../config/apiGateway";

export default function LoginPage() {
  const dispatch = useAppDispatch();

  function handleLogin() {
    const popup = window.open(
      apiGatewayUrl + "/auth/oauth2/authorization/google",
      "viteUrlShortenerOauthLogin",
      "width=500,height=600"
    );

    function receiveMessage(event: MessageEvent) {
      if (event.origin !== apiGatewayUrl) return;

      const { token, email } = event.data;
      if (token && email) {
        dispatch(
          setCredentials({
            status: "authenticated",
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

  function handleMockedServicesLogin() {
    dispatch(setCredentials(fakeAuthenticatedAuthState));
  }

  return (
    <Page>
      <Title content="Login" />
      <Button
        label="Login with Google"
        onClick={handleLogin}
        isDisabled={isMSWEnabled}
      />
      {isMSWEnabled && (
        <>
          <br />
          <br />
          <Button
            label="Mocked Services Login"
            onClick={handleMockedServicesLogin}
          />
        </>
      )}
    </Page>
  );
}
