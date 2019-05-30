-- 执行秒杀存储过程
DELIMITER $$  -- 改变结束标志  默认为 ";"

-- 定义存储过程
CREATE PROCEDURE `seckill`.`execute_seckill` (  -- seckill表中的execute_seckill存储过程
  -- IN定义传入参数， OUT定义返回结果
  IN v_seckill_id BIGINT, IN v_user_phone BIGINT,
  IN v_kill_time TIMESTAMP, OUT r_result INT
)
BEGIN
  DECLARE insert_count INT DEFAULT 0;
  START TRANSACTION;
  insert ignore into `success_killed`(
    seckill_id, user_phone, state, create_time
  ) values (
    v_seckill_id, v_user_phone, 0, v_kill_time
  );
  select row_count() into insert_count;
  IF (insert_count = 0) THEN
    ROLLBACK;
    set r_result = -1; -- 重复秒杀
  ELSEIF (insert_count < 0) THEN
    ROLLBACK;
    set r_result = -2; -- 系统异常
  ELSE
    -- 插入成功 更新减库存
    update `seckill`
    set number = number - 1
    where seckill_id = v_seckill_id
    and end_time > v_kill_time
    and start_time < v_kill_time
    and number > 0;
    select row_count() into insert_count;
    IF (insert_count = 0) THEN
      ROLLBACK;
      set r_result = 0; -- 秒杀结束
    ELSEIF (insert_count < 0) THEN
      ROLLBACK;
      set r_result = -2;
    ELSE
      COMMIT;
      set r_result = 1; -- 秒杀成功
    END IF;
  END IF;
END;
$$

DELIMITER ;  -- 改回原始结束标志

set @r_result = -100;
-- 执行存储过程
call execute_seckill(1001, 12345678902, now(), @r_result);
-- 获取结果
select @r_result;