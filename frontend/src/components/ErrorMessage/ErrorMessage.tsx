import styles from "./ErrorMessage.module.css";

type ErrorMessageProps = {
  message: string;
  suggestedSolution?: string;
};

export default function ErrorMessage({
  message,
  suggestedSolution,
}: ErrorMessageProps) {
  return (
    <section className={styles.content} role="alert">
      <p>Something went wrong: "{message}".</p>
      {suggestedSolution && <p>{suggestedSolution}</p>}
    </section>
  );
}
