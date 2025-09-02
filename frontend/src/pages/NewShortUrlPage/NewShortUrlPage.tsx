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
import { Button } from "../../components/Button/Button";
import "./NewShortUrlPage.css";

function isValidHttpUrl(str: string): boolean {
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
  const isOriginalUrlValid = isValidHttpUrl(originalUrl);
  const validationState: TextInputState =
    originalUrl === "" ? "normal" : isOriginalUrlValid ? "valid" : "error";

  function handleOriginalUrlChange(newLongUrl: string) {
    dispatch(updateOriginalUrl({ originalUrl: newLongUrl.trim() }));
  }

  function handleURLShortenSubmit() {}

  return (
    <>
      <Title content="Shorten an URL"></Title>
      <Text content="Make your links shorter and get a QR code in one click."></Text>
      <div className="original-url-input-container">
        <TextInput
          value={originalUrl}
          onChange={handleOriginalUrlChange}
          placeholder="Enter your link here"
          validationState={validationState}
        />
      </div>
      <div>
        <Button
          label="Shorten URL"
          onClick={handleURLShortenSubmit}
          isDisabled={!isOriginalUrlValid}
        ></Button>
      </div>
    </>
  );
}
