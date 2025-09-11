type ButtonProps = {
  label: string;
  onClick: () => void;
  isDisabled?: boolean;
};

export default function Button({
  label,
  onClick,
  isDisabled = false,
}: ButtonProps) {
  function handleClick() {
    onClick();
  }
  return (
    <button onClick={handleClick} disabled={isDisabled}>
      {label}
    </button>
  );
}
