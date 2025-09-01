import type { ChangeEvent } from "react";

type TextInputProps = {
  value: string;
  placeholder?: string;
  onChange: (newValue: string) => void;
  isDisabled?: boolean;
};

export default function TextInput({
  value,
  placeholder = "",
  onChange,
  isDisabled = false,
}: TextInputProps) {
  function handleChange(e: ChangeEvent<HTMLInputElement>) {
    return onChange(e.target.value);
  }

  return (
    <input
      type="text"
      value={value}
      placeholder={placeholder}
      onChange={handleChange}
      disabled={isDisabled}
    />
  );
}
