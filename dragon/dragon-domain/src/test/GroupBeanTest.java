import dragon.dao.Daos;
import dragon.dao.GroupDao;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class GroupBeanTest {

    GroupDao dao = Daos.get(GroupDao.class);

    @Test
    public void getGroups() throws Exception {
        List ret = dao.getGroups("xzchenglin@gmail.com");
        Arrays.asList("asasd".toCharArray()).toArray();
        System.out.println(ret);
    }


    @Test
    public void sort() {
        int[] arr = {6,7,2,45,45,3,1,12,35,56567,14,34,657,76886,1,231};
        Arrays.stream(mSort(arr)).forEach(System.out::println);

        arr = mSort(arr);
        System.out.println(bSearch(arr, 14, 0, arr.length));

        System.out.println(findDuplicate(arr));

        System.out.println(reverseWords("asdas dfgh adfg fhj ha ds"));
    }

    private int[] bSort(final int[] src){

        for(int i=0; i<src.length; i++){
            for(int j=i; j<src.length; j++){
                if(src[i] > src[j]){
                    int tmp = src[i];
                    src[i] = src[j];
                    src[j] = tmp;
                }
            }
        }

        return src;
    }

    private int[] mSort(final int[] src){
        if(src == null || src.length < 2){
            return src;
        }
        int[] arr1 = Arrays.copyOfRange(src, 0, src.length/2);
        int[] arr2 = Arrays.copyOfRange(src, src.length/2, src.length);

        arr1 = mSort(arr1);
        arr2 = mSort(arr2);
        return merge(arr1, arr2);
    }

    private int[] merge(int[]arr1, int[]arr2){
        int[] ret = new int[arr1.length + arr2.length];

        int i1 = 0;
        int i2 = 0;
        int i = 0;

        while(i2< arr2.length && i1< arr1.length){
            if(arr1[i1] <= arr2[i2]) {
                ret[i++] = arr1[i1++];
            } else {
                ret[i++] = arr2[i2++];
            }
        }

        if(i1 == arr1.length){
            while(i2 < arr2.length){
                ret[i++] = arr2[i2++];
            }
        } else {
            while(i1 < arr1.length){
                ret[i++] = arr1[i1++];
            }
        }
        return ret;
    }

    private int bSearch(final int[] src, int v, int s, int e){
        int i = s + (e-s)/2;
        if(src[i] == v){
            return i;
        }
        if(s == e){
            return -1;
        }
        else if(src[i] < v) {
            return bSearch(src, v, i+1, e);
        } else {
            return bSearch(src, v, s, i);
        }
    }

    public Integer findDuplicate(int[] nums) {
        Set<Integer> set = new HashSet<>();

        for(Integer cc : nums){
            if(set.contains(cc)){
                return cc;
            }
            set.add(cc);
        }

        return null;
    }

    public String reverseWords(String s) {
        return Arrays.stream(s.split(" ")).map(ss -> new StringBuilder(ss).reverse().toString()).collect(Collectors.joining(" "));
    }
}