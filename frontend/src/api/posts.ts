import type { PostStatus } from '../types/postStatus'

export interface Post {
    title: string
    slug: string
    createAt: string
    tags: string[]
}

export interface PostSummary {
    title: string
    slug: string
    createAt: string
}

export interface PostDetail {
    title: string
    slug: string
    createAt: string
    content: string
    tags: string[]
}

export async function fetchPosts(): Promise<Post[]> {
    const response = await fetch(`/api/v1/posts`)
    if (!response.ok) {
        throw new Error(`Failed to fetch posts: ${response.status}`)
    }

    const data: Post[] = await response.json()

    return data
}

export async function fetchPostDetail(slug: string): Promise<PostDetail> {
    const response = await fetch(`/api/v1/posts/${slug}`)
    if (!response.ok) {
        throw new Error(`Failed to fetch post detail: ${response.status}`)
    }

    const data: PostDetail = await response.json()

    return data
}

export async function fetchPostPreview(
    title: string,
    content: string,
    tags: string[]): Promise<PostDetail> {
    const response = await fetch(`/api/v1/posts/preview`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ title, "contentMd": content, tags }),
    })
    if (!response.ok) {
        throw new Error(`Failed to preview post: ${response.status}`)
    }

    return await response.json()
}

export async function createPost(
    title: string,
    content: string,
    tags: string[],
    status: PostStatus
): Promise<Post> {
    const response = await fetch(`/api/v1/posts`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ title, "contentMd": content, tags, status }),
    })
    if (!response.ok) {
        throw new Error(`Failed to create post: ${response.status}`)
    }

    const data: Post = await response.json()

    return data
}
