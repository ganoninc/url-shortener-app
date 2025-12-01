import type { Meta, StoryObj } from "@storybook/react-vite";
import ClickEventsTable from "./ClickEventsTable";
import { fakeClickEventsReponse } from "../../mocks/fakes";
import { action } from "storybook/actions";

const meta: Meta<typeof ClickEventsTable> = {
  title: "Components/ClickEventsTable",
  component: ClickEventsTable,
};
export default meta;

type Story = StoryObj<typeof ClickEventsTable>;

export const Default: Story = {
  args: {
    onLoadMore: action("onLoadMore"),
    clickEvents: fakeClickEventsReponse("abcd123", 10).events.items,
    isLoading: false,
    hasMore: false,
  },
};

export const CanLoadMoreClickEvents: Story = {
  args: {
    onLoadMore: action("onLoadMore"),
    clickEvents: fakeClickEventsReponse("abcd123", 10).events.items,
    isLoading: false,
    hasMore: true,
  },
};

export const IsLoading: Story = {
  args: {
    onLoadMore: action("onLoadMore"),
    clickEvents: fakeClickEventsReponse("abcd123", 10).events.items,
    isLoading: true,
    hasMore: true,
  },
};

export const HasErrorMessageAndHasMoreSetToTrue: Story = {
  args: {
    onLoadMore: action("onLoadMore"),
    clickEvents: fakeClickEventsReponse("abcd123", 10).events.items,
    isLoading: false,
    hasMore: true,
    errorMessage: "Unable to retrieve click events",
  },
};
