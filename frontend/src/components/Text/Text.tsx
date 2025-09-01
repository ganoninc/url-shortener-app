type TextProps = {
  content: string;
};

export default function Text({ content }: TextProps) {
  return <p>{content}</p>;
}
