import { apiFetch } from './client'
import type { PostSummary } from './posts'

export interface CategorySummary {
  categoryName: string
  categoryDesc?: string
  postCount: number
  lastPostCreateAt?: string | null
}

export interface CategoryPosts {
  categoryName: string
  categoryDesc?: string
  postCount: number
  posts: PostSummary[]
}

export async function fetchCategories(): Promise<CategorySummary[]> {
  const response = await apiFetch(`/api/v1/categories/summary`)

  if (!response.ok) {
    throw new Error(`Failed to fetch categories: ${response.status}`)
  }

  const data: CategorySummary[] = await response.json()

  return data
}

export async function fetchCategoryPosts(categoryName: string): Promise<CategoryPosts | null> {
  const response = await apiFetch(`/api/v1/categories/${encodeURIComponent(categoryName)}`)
  if (response.status === 404) {
    return null
  }
  if (!response.ok) {
    throw new Error(`Failed to fetch category posts: ${response.status}`)
  }

  const data: CategoryPosts = await response.json()

  return data
}
