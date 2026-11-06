import type { PostStatus } from '../types/post-status'
import { apiFetch } from './client'

export interface Post {
    id: string
    title: string
    slug: string
    createAt: string
    tags: string[]
}

export interface PostSummary {
    id: string
    title: string
    slug: string
    createAt: string
}

export interface PostDetail {
    id: string | null
    title: string
    slug: string
    createAt: string
    content: string
    contentMd: string
    category: { categoryId: number; categoryName: string } | null
    tags: string[]
}

export async function fetchPosts(): Promise<Post[]> {
    const response = await apiFetch(`/api/v1/posts?statuses=PUBLISHED`)
    if (!response.ok) {
        throw new Error(`Failed to fetch posts: ${response.status}`)
    }

    const data: Post[] = await response.json()

    return data
}

export async function fetchDraftPosts(): Promise<Post[]> {
    const response = await apiFetch(`/api/v1/posts?statuses=DRAFT`)
    if (!response.ok) {
        throw new Error(`Failed to fetch draft posts: ${response.status}`)
    }

    const data: Post[] = await response.json()

    return data
}

export async function fetchPostDetail(id: string): Promise<PostDetail> {
    const response = await apiFetch(`/api/v1/posts/${id}`)
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
    const response = await apiFetch(`/api/v1/posts/preview`, {
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
    categoryId: number,
    tags: string[],
    status: PostStatus
): Promise<Post> {
    const response = await apiFetch(`/api/v1/posts`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ title, "contentMd": content, categoryId, tags, status }),
    })
    if (!response.ok) {
        throw new Error(`Failed to create post: ${response.status}`)
    }

    const data: Post = await response.json()

    return data
}

export async function deletePost(id: string): Promise<void> {
    const response = await apiFetch(`/api/v1/posts/${id}`, { method: 'DELETE' })
    if (!response.ok) {
        throw new Error(`Failed to delete post: ${response.status}`)
    }
}

export async function updatePost(
    id: string,
    title: string,
    content: string,
    categoryId: number,
    tags: string[],
    status: PostStatus
): Promise<Post> {
    const response = await apiFetch(`/api/v1/posts/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ title, "contentMd": content, categoryId, tags, status }),
    })
    if (!response.ok) {
        throw new Error(`Failed to update post: ${response.status}`)
    }

    return await response.json()
}
