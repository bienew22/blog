import { apiFetch } from './client'

export interface CategorySummary {
  categoryName: string
  categoryDesc?: string
  postCount: number
  lastPostCreateAt?: string | null
}

export async function fetchCategories(): Promise<CategorySummary[]> {
  const response = await apiFetch(`/api/v1/categories/summary`)
  if (!response.ok) {
    throw new Error(`Failed to fetch categories: ${response.status}`)
  }

  const data: CategorySummary[] = await response.json()

  return data
}
