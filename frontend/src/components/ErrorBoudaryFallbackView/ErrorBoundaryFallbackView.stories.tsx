import type { Meta, StoryObj } from "@storybook/react-vite";
import ErrorBoudaryFallbackView from "./ErrorBoudaryFallbackView";

const meta: Meta<typeof ErrorBoudaryFallbackView> = {
  title: "Components/ErrorBoudaryFallbackView",
  component: ErrorBoudaryFallbackView,
  tags: ["autodocs"],
  args: {
    error: new Error("FakeError: Unable to contact server."),
  },
};
export default meta;

type Story = StoryObj<typeof ErrorBoudaryFallbackView>;

export const Default: Story = {};
