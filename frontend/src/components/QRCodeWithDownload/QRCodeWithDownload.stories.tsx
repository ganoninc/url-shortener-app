import type { Meta, StoryObj } from "@storybook/react-vite";
import QRCodeWithDownload from "./QRCodeWithDownload";

const meta: Meta<typeof QRCodeWithDownload> = {
  title: "Components/QRCodeWithDownload",
  component: QRCodeWithDownload,
  tags: ["autodocs"],
  parameters: {
    layout: "centered",
  },
  args: {
    value: "http://localhost/",
  },
};
export default meta;

type Story = StoryObj<typeof QRCodeWithDownload>;

export const Default: Story = {};
