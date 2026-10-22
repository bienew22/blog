export const POST_STATUS = {
    PUBLISHED: 'PUBLISHED',
    DRAFT: 'DRAFT',
} as const;

export type PostStatus = (typeof POST_STATUS)[keyof typeof POST_STATUS];