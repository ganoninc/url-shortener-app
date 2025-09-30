import { useEffect, useState } from "react";
import { urlService } from "../../api/client";
import type { UserUrlDTO } from "../../api/url/generated";
import { useNavigate, useParams } from "react-router-dom";
import Title from "../../components/Title/Title";
import loadingSvg from "../../assets/loading.svg";
import ErrorMessage from "../../components/ErrorMessage/ErrorMessage";
import styles from "./ShortUrlPage.module.css";
import { apiGatewayUrl } from "../../config/apiGateway";
import CopyableInput from "../../components/CopyableInput/CopyableInput";
import QRCodeWithDownload from "../../components/QRCodeWithDownload/QRCodeWithDownload";
import Button from "../../components/Button/Button";
import moment from "moment";
import Confetti from "react-confetti-boom";
import { ROUTES } from "../../routePaths";

type ShortUrlPageProps = {
  newShortUrl?: boolean;
};

type ShortUrlPageState =
  | {
      status: "loading";
    }
  | {
      status: "loaded";
      urlDetails: Omit<UserUrlDTO, "shortId"> | null;
    }
  | { status: "error"; errorMessage: string };

export default function ShortUrlPage({
  newShortUrl = false,
}: ShortUrlPageProps) {
  const [pageState, setPageState] = useState<ShortUrlPageState>({
    status: "loading",
  });
  const { shortId } = useParams();
  const navigate = useNavigate();
  const shortUrl = `${apiGatewayUrl}/${shortId}`;

  useEffect(() => {
    if (!shortId) {
      setPageState({ status: "error", errorMessage: "Invalid short url id" });
      return;
    }

    urlService
      .getUserUrl(shortId as string)
      .then((res) => {
        setPageState({
          status: "loaded",
          urlDetails: {
            originalUrl: res.data.originalUrl,
            createdAt: res.data.createdAt,
          },
        });
      })
      .catch((reason) => {
        setPageState({
          status: "error",
          errorMessage: reason.message,
        });
      });
  }, [shortId]);

  return (
    <>
      <Title
        content={
          newShortUrl ? "Your Short Link Is Ready!" : "Short URL Details"
        }
        level="1"
      />
      {pageState.status === "loading" ? (
        <div className={styles.loadingContainer}>
          <Title content="Loading..." level="3" />
          <div>
            <img src={loadingSvg} alt="Loading..." width={64} height={64} />
          </div>
        </div>
      ) : pageState.status === "loaded" ? (
        <div className={styles.container}>
          {newShortUrl && (
            <Confetti
              shapeSize={12}
              spreadDeg={50}
              particleCount={250}
              y={0.25}
              launchSpeed={1.5}
            />
          )}
          <div className={styles.originalUrlAndCreatedAt}>
            {!newShortUrl && (
              <div className={styles.backButton}>
                {" "}
                <Button
                  label={"⬅️ Go back to your short URLs"}
                  onClick={() => navigate(ROUTES.userShortUrlList)}
                />
              </div>
            )}
            <div className={styles.originalUrl}>
              Destination:{" "}
              <a
                href={pageState.urlDetails?.originalUrl}
                target="_blank"
                rel="noopener noreferrer"
              >
                {pageState.urlDetails?.originalUrl}
              </a>
            </div>
            {!newShortUrl && (
              <div className={styles.createdAt}>
                Date created:{" "}
                {moment(pageState.urlDetails?.createdAt).format("LLL")}
              </div>
            )}
          </div>
          <div className={styles.shareShortUrl}>
            <div className={styles.shareAsText}>
              <CopyableInput value={shortUrl} />
            </div>
            <div className={styles.shareAsQRcode}>
              <QRCodeWithDownload value={shortUrl} />
            </div>
          </div>

          {newShortUrl && (
            <div className={styles.suggestedNextActions}>
              {" "}
              <Button
                label="➕ Shorten another URL"
                onClick={() => navigate(ROUTES.home)}
              />
              <Button
                label={"View all your URLs"}
                onClick={() => navigate(ROUTES.userShortUrlList)}
              />
            </div>
          )}
        </div>
      ) : (
        <div className={styles.error}>
          <ErrorMessage message={pageState.errorMessage} />
          <Button onClick={() => navigate(-1)} label="Go back" />
        </div>
      )}
    </>
  );
}
