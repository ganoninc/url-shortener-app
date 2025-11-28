package com.ganoninc.viteurlshortener.analyticsservice.common;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record PaginatedSlice<T, S>(
    @Schema(
            description =
                """
        Items returned in this cursor slice. This list represents only a window
        of the full dataset, limited by the requested page size, and ordered
        consistently according to the API’s contract.
        """,
            requiredMode = Schema.RequiredMode.REQUIRED)
        List<T> items,
    @Schema(
            description =
                """
        Cursor pointing to the next slice. Null when no additional items are available.
        Clients should pass this cursor to retrieve the following page.
        """)
        S nextCursor) {}
