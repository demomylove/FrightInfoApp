#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
24点游戏求解器
使用数字 2, 3, 5, 12 计算24点的所有可能解法
"""

import itertools
from fractions import Fraction


def solve24(numbers):
    """
    计算24点游戏的所有可能解法
    
    Args:
        numbers: 四个数字的列表
        
    Returns:
        所有可能解法的列表
    """
    
    def calculate(a, op, b):
        """执行基本运算"""
        if op == '+':
            return a + b
        elif op == '-':
            return a - b
        elif op == '*':
            return a * b
        elif op == '/':
            if b == 0:
                raise ZeroDivisionError
            return a / b
    
    def is_close_to_24(value):
        """判断结果是否接近24"""
        return abs(value - 24) < 1e-9
    
    def format_expression(nums, ops, pattern):
        """格式化表达式字符串"""
        if pattern == 1:
            # ((a op1 b) op2 c) op3 d
            return f"(({nums[0]} {ops[0]} {nums[1]}) {ops[1]} {nums[2]}) {ops[2]} {nums[3]}"
        elif pattern == 2:
            # (a op1 (b op2 c)) op3 d
            return f"({nums[0]} {ops[0]} ({nums[1]} {ops[1]} {nums[2]})) {ops[2]} {nums[3]}"
        elif pattern == 3:
            # a op1 ((b op2 c) op3 d)
            return f"{nums[0]} {ops[0]} (({nums[1]} {ops[1]} {nums[2]}) {ops[2]} {nums[3]})"
        elif pattern == 4:
            # a op1 (b op2 (c op3 d))
            return f"{nums[0]} {ops[0]} ({nums[1]} {ops[1]} ({nums[2]} {ops[2]} {nums[3]}))"
        elif pattern == 5:
            # (a op1 b) op2 (c op3 d)
            return f"({nums[0]} {ops[0]} {nums[1]}) {ops[1]} ({nums[2]} {ops[2]} {nums[3]})"
    
    def evaluate_expression(nums, ops, pattern):
        """计算表达式的值"""
        try:
            if pattern == 1:
                # ((a op1 b) op2 c) op3 d
                temp1 = calculate(nums[0], ops[0], nums[1])
                temp2 = calculate(temp1, ops[1], nums[2])
                return calculate(temp2, ops[2], nums[3])
            elif pattern == 2:
                # (a op1 (b op2 c)) op3 d
                temp1 = calculate(nums[1], ops[1], nums[2])
                temp2 = calculate(nums[0], ops[0], temp1)
                return calculate(temp2, ops[2], nums[3])
            elif pattern == 3:
                # a op1 ((b op2 c) op3 d)
                temp1 = calculate(nums[1], ops[1], nums[2])
                temp2 = calculate(temp1, ops[2], nums[3])
                return calculate(nums[0], ops[0], temp2)
            elif pattern == 4:
                # a op1 (b op2 (c op3 d))
                temp1 = calculate(nums[2], ops[2], nums[3])
                temp2 = calculate(nums[1], ops[1], temp1)
                return calculate(nums[0], ops[0], temp2)
            elif pattern == 5:
                # (a op1 b) op2 (c op3 d)
                temp1 = calculate(nums[0], ops[0], nums[1])
                temp2 = calculate(nums[2], ops[2], nums[3])
                return calculate(temp1, ops[1], temp2)
        except (ZeroDivisionError, OverflowError):
            return None
    
    solutions = set()
    operations = ['+', '-', '*', '/']
    
    # 遍历所有数字的排列
    for num_perm in itertools.permutations(numbers):
        # 遍历所有运算符的组合
        for op_combo in itertools.product(operations, repeat=3):
            # 尝试5种不同的括号组合方式
            for pattern in range(1, 6):
                try:
                    result = evaluate_expression(num_perm, op_combo, pattern)
                    if result is not None and is_close_to_24(result):
                        expression = format_expression(num_perm, op_combo, pattern)
                        solutions.add(f"{expression} = 24")
                except:
                    continue
    
    return sorted(list(solutions))


def main():
    """主函数"""
    print("24点游戏求解器")
    print("=" * 40)
    
    # 输入的四个数字
    numbers = [2, 3, 5, 12]
    print(f"使用数字: {numbers}")
    print("目标: 24")
    print()
    
    # 求解
    print("正在计算所有可能的解法...")
    solutions = solve24(numbers)
    
    # 输出结果
    if solutions:
        print(f"\n找到 {len(solutions)} 种解法:")
        print("-" * 40)
        for i, solution in enumerate(solutions, 1):
            print(f"{i:2d}. {solution}")
    else:
        print("\n没有找到解法!")
    
    # 验证几个解法
    if solutions:
        print("\n验证前3个解法:")
        print("-" * 40)
        for i, solution in enumerate(solutions[:3], 1):
            expr = solution.split(" = ")[0]
            try:
                result = eval(expr)
                print(f"{i}. {expr} = {result}")
            except:
                print(f"{i}. {expr} = 计算错误")


if __name__ == "__main__":
    main()