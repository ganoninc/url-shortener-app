import type { ChangeEvent, KeyboardEvent } from "react";
import styles from "./TextInput.module.css";

export type TextInputState = "normal" | "valid" | "error";

type TextInputProps = {
  value: string;
  name: string;
  placeholder?: string;
  onChange: (newValue: string) => void;
  onEnterPress?: () => void;
  autocomplete?: boolean;
  isDisabled?: boolean;
  validationState: TextInputState;
};

export default function TextInput({
  value,
  name,
  placeholder = "",
  onChange,
  onEnterPress = () => {},
  autocomplete = true,
  isDisabled = false,
  validationState = "normal",
}: TextInputProps) {
  function handleChange(e: ChangeEvent<HTMLInputElement>) {
    return onChange(e.target.value);
  }

  function handleKeyUp(e: KeyboardEvent<HTMLInputElement>) {
    if (e.key === "Enter") {
      onEnterPress();
    }
  }

  return (
    <div className={styles.container}>
      <input
        name={name}
        type="text"
        value={value}
        placeholder={placeholder}
        onChange={handleChange}
        onKeyUp={handleKeyUp}
        autoComplete={autocomplete ? "on" : "off"}
        disabled={isDisabled}
        aria-invalid={validationState === "error"}
        className={
          validationState === "valid"
            ? styles.isValid
            : validationState === "error"
            ? styles.hasError
            : ""
        }
      />
    </div>
  );
}
