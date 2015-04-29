package ua.yandex.prioritymap;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class PriorityHashMapTest {

    @Test
    public void testSize() throws Exception {
        PriorityHashMap<Integer, String> map = new PriorityHashMap<>(0);
        Assert.assertTrue(map.size() == 0);
        map.put(1, "j");
        map.put(2, null);
        Assert.assertTrue(map.size() == 2);
        map.remove(1);
        Assert.assertTrue(map.size() == 1);
        map.remove("khkhkll");
        Assert.assertTrue(map.size() == 1);
    }

    @Test
    public void testContainsKey() throws Exception {
        PriorityHashMap<String, Integer> map = new PriorityHashMap<>(0);
        Assert.assertFalse(map.containsKey(null));
        Assert.assertFalse(map.containsKey(new Object()));
        String key1 = "some key";
        String key2 = "some key2";
        String key3 = "some key3";
        String key4 = "some key4";
        map.put(key1, 42142);
        map.put(key2, 42142);
        map.put(key3, 42142);
        map.put(key4, 42142);
        Assert.assertTrue(map.containsKey(key1));
        Assert.assertTrue(map.containsKey(key2));
        Assert.assertTrue(map.containsKey(key3));
        Assert.assertTrue(map.containsKey(key4));
        map.remove(key1, 42142);
        map.remove(key2, 42142);
        map.remove(key3, 42142);
        map.remove(key4, 42142);
    }

    @Test
    public void testContainsValue() throws Exception {
        PriorityHashMap<String, Integer> map = new PriorityHashMap<>(0);
        Assert.assertFalse(map.containsValue("something useful"));
        map.put("key1", 1);
        map.put("key1", 2);
        map.put("key2", 3);
        map.put("key3", 4);
        map.put("key4", 5);
        map.put("key4", 6);
        Assert.assertFalse(map.containsValue(1));
        Assert.assertTrue(map.containsValue(2));
        Assert.assertTrue(map.containsValue(3));
        Assert.assertTrue(map.containsValue(4));
        Assert.assertFalse(map.containsValue(5));
        Assert.assertTrue(map.containsValue(6));
    }

    @Test
    public void testGet() throws Exception {
        PriorityHashMap<String, Integer> map = new PriorityHashMap<>(0);
        Assert.assertNull(map.get("something"));
        int size = 6;
        for (int index = 1; index <= size; index++) {
            map.put("key" + index, -index);
        }
        PriorityHashMap<String, Integer> bigMap = new PriorityHashMap<>(0);
        bigMap.putAll(map);
        for (int index = 1; index <= size; index++) {
            Assert.assertEquals(-index, (int) map.get("key" + index));
        }
    }

    @Test
    public void testPut() throws Exception {
        PriorityHashMap<Integer, Integer> map = new PriorityHashMap<>(0, 0.99f);
        int size = 300;
        for (int index = 0; index < size; index++) {
            map.put(index, index + 1);
        }
        for (int index = 0; index < size; index++) {
            Assert.assertEquals(index + 1, (int) map.get(index));
        }
    }

    @Test
    public void testPutNullKey() throws Exception {
        PriorityHashMap<Integer, Integer> map = new PriorityHashMap<>(100, 0.1f);
        map.put(null, 12);
        Assert.assertEquals(12, (int) map.get(null));
    }

    @Test
    public void testPutNullValue() throws Exception {
        PriorityHashMap<Integer, Integer> map = new PriorityHashMap<>(0);
        map.put(null, null);
        Assert.assertEquals(null, map.get(null));
        Assert.assertTrue(map.size() == 1);
    }

    @Test
    public void testRemove() throws Exception {
        PriorityHashMap<Integer, Integer> map = new PriorityHashMap<>(0);
        Assert.assertNull(map.remove(1));
        int size = 300;
        for (int index = 0; index < size; index++) {
            map.put(index, index + 1);
        }
        for (int index = 0; index < size; index++) {
            Assert.assertEquals(index + 1, (int) map.remove(index));
            Assert.assertFalse(map.containsKey(index));
        }
        Assert.assertTrue(map.isEmpty());
    }

    @Test
    public void testRemoveNullKey() throws Exception {
        PriorityHashMap<Integer, Integer> map = new PriorityHashMap<>(0);
        map.put(null, 1);
        Assert.assertEquals(1, (int) map.remove(null));
        Assert.assertFalse(map.containsKey(null));
    }

    @Test
    public void testRemoveKeyFromCenterNotDeleteAnother() throws Exception {
        PriorityHashMap<Integer, Integer> map = new PriorityHashMap<>(0, 0.1f);
        int size = 300;
        for (int index = 0; index < size; index++) {
            map.put(index, index + 1);
        }
        map.put(null, null);
        map.remove(0);
        for (int index = 0; index < size; index++) {
            if (index != 0) {
                Assert.assertTrue(map.containsKey(index));
            }
        }
    }

    @Test
    public void testPutAll() throws Exception {
        PriorityHashMap<Integer, Integer> map = new PriorityHashMap<>(0);
        Assert.assertNull(map.remove(1));
        int size = 300;
        for (int index = 0; index < size; index++) {
            map.put(index, index + 1);
        }
        PriorityHashMap<Integer, Integer> newMap = new PriorityHashMap<>(0);
    }

    @Test
    public void testClear() throws Exception {
        PriorityHashMap<Integer, Integer> map = new PriorityHashMap<>(0);
        int size = 300;
        for (int index = 0; index < size; index++) {
            map.put(index, index + 1);
        }
        map.clear();
        Assert.assertTrue(map.isEmpty());
        for (int index = 0; index < size; index++) {
            Assert.assertFalse(map.containsKey(index));
        }
    }

    @Test
    public void testKeySet() throws Exception {

        PriorityHashMap<Integer, Integer> map = new PriorityHashMap<>(0);
        int size = 300;
        for (int index = 0; index < size; index++) {
            map.put(index, index + 1);
        }
        Set<Integer> keys = map.keySet();
        Assert.assertEquals(size, keys.size());
        for (int index = 0; index < size; index++) {
            Assert.assertTrue(keys.contains(index));
        }
    }

    @Test
    public void testKeySetNullKey() throws Exception {
        PriorityHashMap<Integer, Integer> map = new PriorityHashMap<>(0);
        map.put(null, null);
        map.put(1, 1);
        Set<Integer> keys = map.keySet();
        List<Integer> expectedKeys = Arrays.asList(null, 1);
        Assert.assertTrue(keys.containsAll(expectedKeys));
    }

    @Test
    public void testValues() throws Exception {
        PriorityHashMap<Integer, Integer> map = new PriorityHashMap<>(0);
        List<Integer> expectedValues = new ArrayList<>();
        int size = 300;
        map.put(null, null);
        expectedValues.add(null);
        for (int index = 1; index < size; index++) {
            map.put(index, index + 1);
            expectedValues.add(index + 1);
        }
        Collection<Integer> values = map.values();
        Assert.assertTrue(values.containsAll(expectedValues));
        Assert.assertTrue(expectedValues.containsAll(values));
    }

    @Test
    public void testEntrySetPosition() throws Exception {
        PriorityHashMap<Integer, Integer> map = new PriorityHashMap<>(0);
        int size = 300;
        Random random = new Random();
        for (int index = 1; index < size; index++) {
            int key = random.nextInt();
            map.put(key, index + 1);
        }
        Set<Map.Entry<Integer, Integer>> entrySet = map.entrySet();
        Map.Entry<Integer, Integer> previousEntry = null;
        for (Map.Entry<Integer, Integer> nextEntry : entrySet) {
            if (previousEntry != null) {
                int comparison = nextEntry.getKey()
                        .compareTo(
                                previousEntry.getKey());
                Assert.assertTrue(comparison == 1);
            }
            previousEntry = nextEntry;
        }
    }

    @Test
    public void testEntrySetNullKeyPosition() throws Exception {
        PriorityHashMap<Integer, Integer> map = new PriorityHashMap<>(0);
        int size = 10;
        Random random = new Random();
        for (int index = 1; index < size; index++) {
            int key = random.nextInt();
            map.put(key, index + 1);
        }
        map.put(null, null);
        Set<Map.Entry<Integer, Integer>> entrySet = map.entrySet();
        Iterator<Map.Entry<Integer, Integer>> iterator = entrySet.iterator();
        Assert.assertNull(iterator.next().getKey());
    }

    @Test
    public void testPeekValueWithPriorityKey() throws Exception {
        PriorityHashMap<Integer, Integer> map = new PriorityHashMap<>(0);
        int size = 300;
        map.put(null, null);
        for (int index = 0; index < size; index++) {
            map.put(index, index + 1);
        }
        for (int index = size; index > 0; index--) {
            Assert.assertEquals(index, (int) map.peekValueWithPriorityKey());
            map.remove(index - 1);
        }
        Assert.assertNull(map.peekValueWithPriorityKey());
    }

    @Test
    public void testPollValueWithPriorityKey() throws Exception {
        PriorityHashMap<Integer, Integer> map = new PriorityHashMap<>(0);
        int size = 300;
        map.put(null, null);
        for (int index = 0; index < size; index++) {
            map.put(index, index + 1);
        }
        for (int index = size; index > 0; index--) {
            Assert.assertEquals(index, (int) map.pollValueWithPriorityKey());
        }
        Assert.assertNull(map.pollValueWithPriorityKey());
    }
}