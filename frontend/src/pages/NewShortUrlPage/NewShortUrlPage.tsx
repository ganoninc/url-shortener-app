import Title from "../../components/Title/Title";
import Text from "../../components/Text/Text";
import TextInput, {
  type TextInputState,
} from "../../components/TextInput/TextInput";
import {
  selectOriginalUrl,
  updateOriginalUrl,
} from "../../redux/newShortUrlSlice";
import { useAppDispatch, useAppSelector } from "../../hooks/hooks";
import Button from "../../components/Button/Button";
import styles from "./NewShortUrlPage.module.css";
import { urlService } from "../../api/client";
import { useState } from "react";
import loadingSvg from "../../assets/loading.svg";
import Page from "../../components/Page/Page";
import { useNavigate } from "react-router-dom";
import { ROUTES } from "../../routePaths";
import { useLoggedIn } from "../../hooks/useLoggedIn";
import ErrorMessage from "../../components/ErrorMessage/ErrorMessage";

type NewShortUrlPageState =
  | {
      status: "initial";
    }
  | { status: "loading" }
  | { status: "error"; errorMessage: string };

function isValidHttpUrl(str: string) {
  try {
    const url = new URL(str);
    return url.protocol === "http:" || url.protocol === "https:";
  } catch {
    return false;
  }
}

export default function NewShortUrlPage() {
  const originalUrl = useAppSelector(selectOriginalUrl);
  const dispatch = useAppDispatch();
  const [pageState, setPageState] = useState<NewShortUrlPageState>({
    status: "initial",
  });
  const navigate = useNavigate();
  const isLoggedIn = useLoggedIn();

  const isOriginalUrlValid = isValidHttpUrl(originalUrl);
  const UrlInputState: TextInputState =
    originalUrl === "" ? "normal" : isOriginalUrlValid ? "valid" : "error";

  function handleUrlInputChange(url: string) {
    dispatch(updateOriginalUrl({ originalUrl: url.trim() }));
  }

  function handleUrlInputEnterPress() {
    if (isOriginalUrlValid) {
      handleUrlSubmit();
    }
  }

  function handleUrlSubmit() {
    if (!isLoggedIn) {
      window.alert("You must log in before shortening a URL.");
      navigate(ROUTES.login);
      return;
    }

    setPageState({ status: "loading" });

    urlService
      .shortenUrl({
        originalUrl,
      })
      .then((res) => {
        setPageState({ status: "initial" });
        dispatch(updateOriginalUrl({ originalUrl: "" }));
        navigate(ROUTES.userNewShortUrlCreated(res.data.shortId));
      })
      .catch((reason) => {
        const message =
          reason instanceof Error
            ? reason.message
            : typeof reason === "string"
            ? reason
            : JSON.stringify(reason);
        setPageState({ status: "error", errorMessage: message });
      });
  }

  return (
    <Page>
      <Title content="Shorten an URL"></Title>
      <Text content="Make your links shorter and get a QR code in one click."></Text>
      <div className={styles.originalUrlInputContainer}>
        <TextInput
          name="original-url"
          value={originalUrl}
          onChange={handleUrlInputChange}
          onEnterPress={handleUrlInputEnterPress}
          placeholder="Enter your link here"
          validationState={UrlInputState}
          autocomplete={false}
          isDisabled={pageState.status === "loading"}
        />
      </div>
      <div className={styles.submitBtnContainer}>
        <Button
          label="Shorten URL"
          onClick={handleUrlSubmit}
          isDisabled={pageState.status === "loading" || !isOriginalUrlValid}
        ></Button>
      </div>

      {pageState.status === "error" && (
        <ErrorMessage
          message={pageState.errorMessage}
          suggestedSolution="Please try again in a few minutes."
        />
      )}

      {pageState.status === "loading" && (
        <div>
          <img src={loadingSvg} alt="Loading..." width={64} height={64} />
        </div>
      )}
    </Page>
  );
}
