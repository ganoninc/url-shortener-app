import { useRef } from "react";
import styles from "./CopyableInput.module.css";

type CopyableInputProps = {
  value: string;
};

export default function CopyableInput({ value }: CopyableInputProps) {
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
    <div className={styles.container}>
      <input
        className={styles.textInput}
        ref={textInputRef}
        value={value}
        disabled={true}
      />
      <button onClick={handleClick}>Copy to clipboard</button>
    </div>
  );
}
