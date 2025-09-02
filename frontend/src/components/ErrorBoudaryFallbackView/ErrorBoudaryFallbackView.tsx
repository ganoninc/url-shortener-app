import Title from "../Title/Title";

type ErrorBoudaryFallbackViewProps = {
  error: Error;
};

export function ErrorBoudaryFallbackView({
  error,
}: ErrorBoudaryFallbackViewProps) {
  return (
    <>
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
        <pre style={{ color: "red" }}>Error message: "{error.message}"</pre>
      </div>
    </>
  );
}
