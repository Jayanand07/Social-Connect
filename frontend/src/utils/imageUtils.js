/**
 * Converts a raw Supabase Storage URL into an optimized transform URL.
 * Supabase resizes and converts to WebP on-the-fly (free on Pro plan).
 *
 * Usage:
 *   import { getOptimizedUrl } from '../utils/imageUtils';
 *   <img src={getOptimizedUrl(user.avatarUrl, 200)} />
 */
export function getOptimizedUrl(url, width = 400, quality = 80) {
  if (!url || typeof url !== 'string') return url;
  if (!url.includes('supabase.co/storage')) return url; // not a Supabase URL

  // Replace /object/public/ with /object/transform/v1/public/
  const optimized = url.replace(
    '/storage/v1/object/public/',
    '/storage/v1/object/transform/v1/public/'
  );

  // Append transformation params
  const separator = optimized.includes('?') ? '&' : '?';
  return `${optimized}${separator}width=${width}&resize=cover&format=webp&quality=${quality}`;
}

/**
 * Returns appropriate width based on usage context.
 * Use this to avoid guessing sizes.
 */
export const IMAGE_SIZES = {
  avatar_small: 48,    // notification bell, comment avatars
  avatar_medium: 150,  // profile preview cards
  avatar_large: 300,   // full profile page
  post_thumbnail: 400, // post feed images
  post_full: 1080,     // full post view / lightbox
  story: 720,          // story viewer
};
