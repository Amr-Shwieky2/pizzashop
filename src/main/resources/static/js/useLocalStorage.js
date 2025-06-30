export const useLocalStorage = (key, initialValue) => {
  const readValue = (targetKey = key, fallback = initialValue) => {
    if (typeof window === "undefined") return fallback;
    try {
      const item = localStorage.getItem(targetKey);
      return item ? JSON.parse(item) : fallback;
    } catch (error) {
      console.warn(`Error reading localStorage key "${targetKey}":`, error);
      return fallback;
    }
  };

  const writeValue = (targetKey = key, value) => {
    try {
      localStorage.setItem(targetKey, JSON.stringify(value));
    } catch (error) {
      console.warn(`Error setting localStorage key "${targetKey}":`, error);
    }
  };

  const removeValue = (targetKey = key) => {
    try {
      localStorage.removeItem(targetKey);
    } catch (error) {
      console.warn(`Error removing localStorage key "${targetKey}":`, error);
    }
  };

  const resetValue = () => writeValue(key, initialValue);

  return {
    readValue,
    writeValue,
    removeValue,
    resetValue,
  };
};

export default useLocalStorage;
