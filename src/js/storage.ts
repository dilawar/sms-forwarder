/* Storage API. */

import { Preferences } from '@capacitor/preferences';
import { type Rule, type Message } from './types';
import hash from 'object-hash';

const RULE_KEY: string = 'RULES';

const KEY_PREFIX_SMS = 'SMS::';

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

/**
 * Delete a given rule and return the rules from the database.
 */
export const deleteRule = async (rule: Rule): Promise<Rule[]> => {
  // prompt use if they are sure

  let rules = (await loadRules()) || [];
  console.debug('[STORAGE] deleting ', JSON.stringify(rule), ' from ', JSON.stringify(rules));

  rules = rules.filter((item) => {
    // This must be true in most versions.
    if (rule?.id) {
      return rule?.id === item?.id;
    }
    // delete this after testing and retiring old versions.
    return item.glob !== rule.glob;
  });
  console.debug('[STORAGE] after deleting ', JSON.stringify(rules));
  await storeRules(rules);
  return rules;
};

/**
 * Save a processed message to store
 */
export const storeMessage = async (message: Message) => {
  console.debug('Storing message');
  const key = KEY_PREFIX_SMS + hash(message);
  await store<Message>(key, message);
};

/**
 * Load stored messages
 */
export const loadMessages = async (): Promise<Message[]> => {
  console.info('[storage] Loading messages from Preferences...');
  const result: Message[] = [];
  const keys = (await Preferences.keys()).keys;
  console.debug('[storage] Found keys ' + JSON.stringify(keys));
  for (const key of keys) {
    if (key.startsWith(KEY_PREFIX_SMS)) {
      const msg = await load<Message>(key);
      if (msg?.sender && msg?.body) {
        result.push(msg);
      }
    }
  }
  console.info(`[storage] Total ${result.length} messages loaded.`);
  return result;
};

async function store<T>(key: string, value: T) {
  await Preferences.set({ key: key, value: JSON.stringify(value) });
}

async function load<T>(key: string): Promise<T | null> {
  const { value } = await Preferences.get({ key: key });
  if (!value) {
    return null;
  }
  return JSON.parse(value);
}
