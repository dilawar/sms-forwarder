/* Storage API. */

import { Preferences } from '@capacitor/preferences';
import { type Rule } from './types';

const RULE_KEY: string = 'RULES';

/**
 * Store a rule
 */
export const storeRules = async (rules: Rule[]) => {
  await store<Rule[]>(RULE_KEY, rules);
};

/**
 * Load stored rule at key
 */
export const loadRules = async (): Promise<Rule[] | null> => {
  return await load<Rule[]>(RULE_KEY);
};

export async function store<T>(key: string, value: T) {
  await Preferences.set({ key: key, value: JSON.stringify(value) });
}

export async function load<T>(key: string): Promise<T | null> {
  const { value } = await Preferences.get({ key: key });
  if (!value) {
    return null;
  }
  return JSON.parse(value);
}
