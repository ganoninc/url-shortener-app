import type { ChangeEvent } from "react";
import styles from "./TextInput.module.css";

export type TextInputState = "normal" | "valid" | "error";

type TextInputProps = {
  value: string;
  name: string;
  placeholder?: string;
  onChange: (newValue: string) => void;
  isDisabled?: boolean;
  validationState: TextInputState;
};

export default function TextInput({
  value,
  name,
  placeholder = "",
  onChange,
  isDisabled = false,
  validationState = "normal",
}: TextInputProps) {
  function handleChange(e: ChangeEvent<HTMLInputElement>) {
    return onChange(e.target.value);
  }

  return (
    <div className={styles.container}>
      <input
        name={name}
        type="text"
        value={value}
        placeholder={placeholder}
        onChange={handleChange}
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
