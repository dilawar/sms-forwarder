/**
 * Types
 */

/**
 * Rule
 */
export type Rule = {
  glob: string;
  address: string;
};

/**
 * Factory function to create a default Rule
 */
export const createRule = (glob: string = '', address: string = ''): Rule => {
  return { glob: glob, address: address };
};

/**
 * SMS type
 */
export type Message = {
  id?: string,
  sender: string;
  message: string;
  body?: string;
  date?: number;
  address?: string;
};

