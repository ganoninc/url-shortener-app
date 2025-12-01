import type { ClickEventDTO } from "../../api/analytics/generated";
import Table from "../Table/Table";
import moment from "moment";
import Button from "../Button/Button";
import loadingSvg from "../../assets/loading.svg";
import ErrorMessage from "../ErrorMessage/ErrorMessage";
import styles from "./ClickEventsTable.module.css";

type ClickEventsTableProps = {
  clickEvents: ClickEventDTO[];
  onLoadMore: () => void;
  isLoading: boolean;
  hasMore: boolean;
  errorMessage?: string;
};

export default function ClickEventsTable({
  clickEvents,
  onLoadMore,
  isLoading,
  hasMore,
  errorMessage,
}: ClickEventsTableProps) {
  return (
    <>
      <Table
        headers={["Date", "Country", "User Agent"] as const}
        rows={clickEvents.map((clickEvent) => {
          return [
            moment(clickEvent.timestamp).format("LLL"),
            clickEvent.countryCode,
            clickEvent.userAgent,
          ] as const;
        })}
      />
      <div className={styles.loadMoreSection}>
        {hasMore && (
          <Button
            label="Load more"
            onClick={() => onLoadMore()}
            isDisabled={isLoading || !!errorMessage}
          />
        )}
        {isLoading && (
          <img src={loadingSvg} alt="Loading..." width={64} height={64} />
        )}
        {errorMessage && <ErrorMessage message={errorMessage} />}
      </div>
    </>
  );
}
