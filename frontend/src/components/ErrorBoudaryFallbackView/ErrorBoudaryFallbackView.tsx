import Page from "../Page/Page";
import Title from "../Title/Title";
import styles from "./ErrorBoundaryFallbackView.module.css";

type ErrorBoudaryFallbackViewProps = {
  error: Error;
};

export default function ErrorBoudaryFallbackView({
  error,
}: ErrorBoudaryFallbackViewProps) {
  return (
    <Page>
      <Title content="Something went wrong." level="2" />
      <div role="alert">
        <p>
          Please refresh the page and try again.
          <br />
          If the problem persists, visit the project’s GitHub{" "}
          <a
            href="https://github.com/ganoninc/url-shortener-app/issues"
            target="_blank"
          >
            issue page
          </a>
          .
        </p>
        <pre className={styles.errorMessage}>
          Error message: "{error.message}"
        </pre>
      </div>
    </Page>
  );
}
