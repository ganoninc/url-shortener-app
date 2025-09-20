import type { Meta, StoryObj } from "@storybook/react-vite";
import ErrorMessage from "./ErrorMessage";

const meta: Meta<typeof ErrorMessage> = {
  title: "Components/ErrorMessage",
  component: ErrorMessage,
  tags: ["autodocs"],
  args: {
    message: "FakeError: Unable to contact server.",
  },
};
export default meta;

type Story = StoryObj<typeof ErrorMessage>;

export const Default: Story = {};

export const WithSuggestedSolution: Story = {
  args: {
    suggestedSolution:
      "Here is a suggested solution related to the error message.",
  },
};
