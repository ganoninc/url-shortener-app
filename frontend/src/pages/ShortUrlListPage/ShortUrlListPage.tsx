import { useEffect, useState } from "react";
import type { UserUrlDTO } from "../../api/url/generated";
import { urlService } from "../../api/client";
import Title from "../../components/Title/Title";
import ErrorMessage from "../../components/ErrorMessage/ErrorMessage";
import loadingSvg from "../../assets/loading.svg";
import styles from "./ShortUrlListPage.module.css";
import Table from "../../components/Table/Table";
import Button from "../../components/Button/Button";
import { useNavigate } from "react-router-dom";
import { ROUTES } from "../../routePaths";
import { apiGatewayUrl } from "../../config/apiGateway";
import moment from "moment";
import CopyableInput from "../../components/CopyableInput/CopyableInput";

type ShortUrlListPageState =
  | {
      status: "loading";
    }
  | {
      status: "loaded";
      shortUrls: UserUrlDTO[];
    }
  | { status: "error"; errorMessage: string };

export default function ShortUrlListPage() {
  const [pageState, setPageState] = useState<ShortUrlListPageState>({
    status: "loading",
  });
  const navigate = useNavigate();

  useEffect(() => {
    urlService
      .getUserUrls()
      .then((res) => {
        setPageState({
          status: "loaded",
          shortUrls: res.data,
        });
      })
      .catch((reason) => {
        setPageState({
          status: "error",
          errorMessage: reason.message,
        });
      });
  }, []);

  return (
    <>
      <Title content="My URLs" level="1" />
      {pageState.status === "loading" ? (
        <div className={styles.loadingContainer}>
          <Title content="Loading..." level="3" />
          <div>
            <img src={loadingSvg} alt="Loading..." width={64} height={64} />
          </div>
        </div>
      ) : pageState.status === "loaded" ? (
        <div className={styles.container}>
          <Button
            label="Shorten another URL"
            onClick={() => navigate(ROUTES.home)}
          />
          <Table
            headers={
              ["Short URL", "Destination", "Date Created", "Actions"] as const
            }
            rows={pageState.shortUrls.map((shortUrl) => {
              return [
                <CopyableInput
                  value={`${apiGatewayUrl}/${shortUrl.shortId}`}
                  inline
                  hideCopyButton
                />,
                <a
                  href={shortUrl.originalUrl}
                  target="_blank"
                  rel="noopener noreferrer"
                  title={shortUrl.originalUrl}
                >
                  {shortUrl.originalUrl}
                </a>,
                moment(shortUrl.createdAt).format("LLL"),
                <Button
                  label="See details"
                  onClick={() => {
                    navigate(ROUTES.myUrlDetail(shortUrl.shortId));
                  }}
                />,
              ] as const;
            })}
          />
        </div>
      ) : (
        <div className={styles.error}>
          <ErrorMessage message={pageState.errorMessage} />
        </div>
      )}
    </>
  );
}
