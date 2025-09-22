type CopyableInputProps = {
  value: string;
};

export default function CopyableInput({ value }: CopyableInputProps) {
  return (
    <>
      <input value={value} disabled={true} />
    </>
  );
}
