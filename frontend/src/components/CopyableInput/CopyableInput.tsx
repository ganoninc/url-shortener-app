import { useRef } from "react";
import styles from "./CopyableInput.module.css";
import Button from "../Button/Button";

type CopyableInputProps = {
  value: string;
  inline?: boolean;
  hideCopyButton?: boolean;
};

export default function CopyableInput({
  value,
  inline = false,
  hideCopyButton = false,
}: CopyableInputProps) {
  const textInputRef = useRef<HTMLInputElement>(null);

  function handleClick() {
    if (textInputRef.current) {
      textInputRef.current.select();
      textInputRef.current.setSelectionRange(0, 99999);
      navigator.clipboard.writeText(textInputRef.current.value);
      window.alert("The URL has been copied to your clipboard");
    }
  }

  return (
    <div
      className={
        inline
          ? `${styles.container} ${styles.containerInline}`
          : styles.container
      }
    >
      <input
        className={styles.textInput}
        ref={textInputRef}
        value={value}
        disabled={true}
      />
      {!hideCopyButton ? (
        <Button label={"Copy to clipboard"} onClick={handleClick} />
      ) : (
        <></>
      )}
    </div>
  );
}
