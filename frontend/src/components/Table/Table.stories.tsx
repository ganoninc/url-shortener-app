import type { Meta, StoryObj } from "@storybook/react-vite";
import Table from "./Table";
import { fakeUserUrlListResonse } from "../../mocks/fakes";
import Button from "../Button/Button";
import { action } from "storybook/internal/actions";

const meta: Meta<typeof Table> = {
  title: "Components/Table",
  component: Table,
  tags: ["autodocs"],
  parameters: {
    layout: "centered",
  },
  args: {
    headers: ["Short Url", "Destination", "Created at", "Actions"] as const,
    rows: fakeUserUrlListResonse.map((shortUrl) => {
      return [
        `http://localhost/${shortUrl.shortId}`,
        <a href={shortUrl.originalUrl} target="_blank">
          {shortUrl.originalUrl}
        </a>,
        shortUrl.createdAt,
        <Button label="Test Btn" onClick={action("on-click")} />,
      ];
    }),
  },
};
export default meta;

type Story = StoryObj<typeof Table>;

export const Default: Story = {};
