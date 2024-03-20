package cn.kimmking.kkrpc.core.registry;

import cn.kimmking.kkrpc.core.meta.InstanceMeta;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Description for this class.
 *
 * @Author : kimmking(kimmking@apache.org)
 * @create 2024/3/17 21:54
 */

@Data
@AllArgsConstructor
public class Event {
    List<InstanceMeta> data;
}
